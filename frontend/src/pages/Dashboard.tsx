import { useState, useMemo, useCallback } from "react"
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query"
import { tasksApi } from "@/services/api"
import { Navbar } from "@/components/Navbar"
import { TaskCard } from "@/components/TaskCard"
import { TaskForm } from "@/components/TaskForm"
import { TaskFilters } from "@/components/TaskFilters"
import { Button } from "@/components/ui/button"
import { Plus, Loader2, AlertCircle } from "lucide-react"
import { toast } from "@/hooks/use-toast"
import type { Task, TaskRequest, TaskFilters as TaskFiltersType } from "@/types/task"

export function DashboardPage() {
  const queryClient = useQueryClient()
  const [filters, setFilters] = useState<TaskFiltersType>({
    status: "",
    priority: "",
    search: "",
  })
  const [formOpen, setFormOpen] = useState(false)
  const [editingTask, setEditingTask] = useState<Task | null>(null)

  // Fetch tasks
  const {
    data: tasks,
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: ["tasks"],
    queryFn: () => tasksApi.list(),
  })

  // Create task mutation
  const createMutation = useMutation({
    mutationFn: (data: TaskRequest) => tasksApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["tasks"] })
      setFormOpen(false)
      toast({ title: "Task created successfully", variant: "success" })
    },
    onError: (err: Error) => {
      toast({ title: "Failed to create task", description: err.message, variant: "destructive" })
    },
  })

  // Update task mutation
  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: TaskRequest }) =>
      tasksApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["tasks"] })
      setFormOpen(false)
      setEditingTask(null)
      toast({ title: "Task updated successfully", variant: "success" })
    },
    onError: (err: Error) => {
      toast({ title: "Failed to update task", description: err.message, variant: "destructive" })
    },
  })

  // Delete task mutation
  const deleteMutation = useMutation({
    mutationFn: (id: number) => tasksApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["tasks"] })
      toast({ title: "Task deleted successfully", variant: "success" })
    },
    onError: (err: Error) => {
      toast({ title: "Failed to delete task", description: err.message, variant: "destructive" })
    },
  })

  // Filter tasks client-side
  const filteredTasks = useMemo(() => {
    if (!tasks) return []
    return tasks.filter((task) => {
      if (filters.status && task.status !== filters.status) return false
      if (filters.priority && task.priority !== filters.priority) return false
      if (filters.search) {
        const search = filters.search.toLowerCase()
        return (
          task.title.toLowerCase().includes(search) ||
          (task.description && task.description.toLowerCase().includes(search))
        )
      }
      return true
    })
  }, [tasks, filters])

  const handleCreate = () => {
    setEditingTask(null)
    setFormOpen(true)
  }

  const handleEdit = (task: Task) => {
    setEditingTask(task)
    setFormOpen(true)
  }

  const handleDelete = useCallback(
    (id: number) => {
      if (confirm("Are you sure you want to delete this task?")) {
        deleteMutation.mutate(id)
      }
    },
    [deleteMutation]
  )

  const handleFormSubmit = async (data: TaskRequest) => {
    if (editingTask) {
      await updateMutation.mutateAsync({ id: editingTask.id, data })
    } else {
      await createMutation.mutateAsync(data)
    }
  }

  const isMutating = createMutation.isPending || updateMutation.isPending

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <main className="max-w-6xl mx-auto px-4 py-6">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h1 className="text-2xl font-bold">Dashboard</h1>
            <p className="text-sm text-muted-foreground">
              Manage your tasks
            </p>
          </div>
          <Button onClick={handleCreate}>
            <Plus className="h-4 w-4 mr-2" />
            New Task
          </Button>
        </div>

        <TaskFilters filters={filters} onChange={setFilters} />

        {isLoading ? (
          <div className="flex items-center justify-center py-20">
            <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
          </div>
        ) : isError ? (
          <div className="flex flex-col items-center justify-center py-20 text-destructive">
            <AlertCircle className="h-12 w-12 mb-4" />
            <p className="text-lg font-medium">Failed to load tasks</p>
            <p className="text-sm text-muted-foreground">
              {error instanceof Error ? error.message : "An unexpected error occurred"}
            </p>
          </div>
        ) : filteredTasks.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 text-muted-foreground">
            <p className="text-lg font-medium">No tasks found</p>
            <p className="text-sm mb-4">
              {tasks?.length === 0
                ? "Create your first task to get started"
                : "Try adjusting your filters"}
            </p>
            {tasks?.length === 0 && (
              <Button onClick={handleCreate} variant="outline">
                <Plus className="h-4 w-4 mr-2" />
                Create Task
              </Button>
            )}
          </div>
        ) : (
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {filteredTasks.map((task) => (
              <TaskCard
                key={task.id}
                task={task}
                onEdit={handleEdit}
                onDelete={handleDelete}
              />
            ))}
          </div>
        )}
      </main>

      <TaskForm
        open={formOpen}
        onOpenChange={(open) => {
          setFormOpen(open)
          if (!open) setEditingTask(null)
        }}
        onSubmit={handleFormSubmit}
        task={editingTask}
        loading={isMutating}
      />
    </div>
  )
}
