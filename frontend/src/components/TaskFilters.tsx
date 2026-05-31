import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Search, X } from "lucide-react"
import { Button } from "@/components/ui/button"
import type { TaskFilters } from "@/types/task"

interface TaskFiltersProps {
  filters: TaskFilters
  onChange: (filters: TaskFilters) => void
}

export function TaskFilters({ filters, onChange }: TaskFiltersProps) {
  const updateFilter = (key: keyof TaskFilters, value: string) => {
    onChange({ ...filters, [key]: value })
  }

  const clearFilters = () => {
    onChange({ status: "", priority: "", search: "" })
  }

  const hasFilters = filters.status || filters.priority || filters.search

  return (
    <div className="flex flex-wrap items-end gap-3 mb-6">
      <div className="flex-1 min-w-[200px]">
        <Label htmlFor="search" className="sr-only">Search</Label>
        <div className="relative">
          <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
          <Input
            id="search"
            placeholder="Search tasks..."
            value={filters.search}
            onChange={(e) => updateFilter("search", e.target.value)}
            className="pl-8"
          />
        </div>
      </div>

      <div className="w-[180px]">
        <Label htmlFor="status-filter" className="sr-only">Status</Label>
        <Select
          value={filters.status}
          onValueChange={(v) => updateFilter("status", v)}
        >
          <SelectTrigger id="status-filter">
            <SelectValue placeholder="All Statuses" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="">All Statuses</SelectItem>
            <SelectItem value="TODO">To Do</SelectItem>
            <SelectItem value="IN_PROGRESS">In Progress</SelectItem>
            <SelectItem value="DONE">Done</SelectItem>
            <SelectItem value="CANCELLED">Cancelled</SelectItem>
          </SelectContent>
        </Select>
      </div>

      <div className="w-[180px]">
        <Label htmlFor="priority-filter" className="sr-only">Priority</Label>
        <Select
          value={filters.priority}
          onValueChange={(v) => updateFilter("priority", v)}
        >
          <SelectTrigger id="priority-filter">
            <SelectValue placeholder="All Priorities" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="">All Priorities</SelectItem>
            <SelectItem value="LOW">Low</SelectItem>
            <SelectItem value="MEDIUM">Medium</SelectItem>
            <SelectItem value="HIGH">High</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {hasFilters && (
        <Button variant="ghost" size="icon" onClick={clearFilters} title="Clear filters">
          <X className="h-4 w-4" />
        </Button>
      )}
    </div>
  )
}
