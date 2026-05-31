import type { Task, TaskStatus, TaskPriority } from "@/types/task"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Pencil, Trash2 } from "lucide-react"
import { format } from "date-fns"

interface TaskCardProps {
  task: Task
  onEdit: (task: Task) => void
  onDelete: (id: number) => void
}

const statusLabels: Record<TaskStatus, string> = {
  TODO: "To Do",
  IN_PROGRESS: "In Progress",
  DONE: "Done",
  CANCELLED: "Cancelled",
}

const statusColors: Record<TaskStatus, string> = {
  TODO: "bg-yellow-100 text-yellow-800 border-yellow-200",
  IN_PROGRESS: "bg-blue-100 text-blue-800 border-blue-200",
  DONE: "bg-green-100 text-green-800 border-green-200",
  CANCELLED: "bg-gray-100 text-gray-800 border-gray-200",
}

const priorityColors: Record<TaskPriority, string> = {
  LOW: "bg-slate-100 text-slate-800 border-slate-200",
  MEDIUM: "bg-orange-100 text-orange-800 border-orange-200",
  HIGH: "bg-red-100 text-red-800 border-red-200",
}

export function TaskCard({ task, onEdit, onDelete }: TaskCardProps) {
  return (
    <Card className="hover:shadow-md transition-shadow">
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between gap-2">
          <CardTitle className="text-base">{task.title}</CardTitle>
          <div className="flex items-center gap-1 shrink-0">
            <Button
              variant="ghost"
              size="icon"
              className="h-8 w-8"
              onClick={() => onEdit(task)}
            >
              <Pencil className="h-4 w-4" />
            </Button>
            <Button
              variant="ghost"
              size="icon"
              className="h-8 w-8 text-destructive hover:text-destructive"
              onClick={() => onDelete(task.id)}
            >
              <Trash2 className="h-4 w-4" />
            </Button>
          </div>
        </div>
      </CardHeader>
      <CardContent className="pb-3">
        {task.description && (
          <p className="text-sm text-muted-foreground line-clamp-2 mb-3">
            {task.description}
          </p>
        )}
        <div className="flex flex-wrap gap-2">
          <Badge variant="outline" className={statusColors[task.status]}>
            {statusLabels[task.status]}
          </Badge>
          <Badge variant="outline" className={priorityColors[task.priority]}>
            {task.priority}
          </Badge>
        </div>
      </CardContent>
      <CardFooter className="pt-0">
        <div className="flex items-center justify-between w-full text-xs text-muted-foreground">
          {task.dueDate && (
            <span>Due: {format(new Date(task.dueDate), "MMM d, yyyy")}</span>
          )}
          <span>
            {format(new Date(task.createdAt), "MMM d, yyyy")}
          </span>
        </div>
      </CardFooter>
    </Card>
  )
}
