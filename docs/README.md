# Atlas User Guide

Atlas is a personal task management chatbot that helps you keep track of your tasks through a friendly graphical interface. Whether you need to manage simple todos, track deadlines, or schedule events, Atlas has got you covered!

## Quick Start

1. **Launch Atlas**: Run the application to open the GUI window
2. **Type your commands**: Use the text field at the bottom to interact with Atlas
3. **Press Enter or click Send**: Submit your command to Atlas
4. **View responses**: Atlas will respond in the chat area with colored dialog boxes

## Features

### Adding Tasks

#### Todo Tasks
Add simple tasks without any specific timing.

**Command**: `todo <description>`

**Example**: `todo read book`

**Expected Output**:
```
Got it. I've added this task:
 [T][ ] read book
Now you have 1 tasks in the list.
```

#### Deadline Tasks
Add tasks with specific due dates.

**Command**: `deadline <description> /by <yyyy-MM-dd>`

**Example**: `deadline return book /by 2025-10-15`

**Expected Output**:
```
Got it. I've added this task:
 [D][ ] return book (by: Oct 15 2025)
Now you have 2 tasks in the list.
```

**Note**: Dates must be in ISO format (yyyy-MM-dd) but are displayed in a user-friendly format (MMM d yyyy).

#### Event Tasks
Add tasks that occur during a specific time period.

**Command**: `event <description> /from <start time> /to <end time>`

**Example**: `event project meeting /from Mon 2pm /to 4pm`

**Expected Output**:
```
Got it. I've added this task:
 [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
```

### Managing Tasks

#### View All Tasks
Display all your tasks in a numbered list.

**Command**: `list`

**Expected Output**:
```
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 15 2025)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

#### Mark Tasks as Done
Mark a specific task as completed.

**Command**: `mark <task number>`

**Example**: `mark 2`

**Expected Output**:
```
Nice! I've marked this task as done:
 [D][X] return book (by: Oct 15 2025)
```

#### Mark Tasks as Not Done
Unmark a completed task.

**Command**: `unmark <task number>`

**Example**: `unmark 2`

**Expected Output**:
```
OK, I've marked this task as not done yet:
 [D][ ] return book (by: Oct 15 2025)
```

#### Delete Tasks
Remove a task from your list.

**Command**: `delete <task number>`

**Example**: `delete 3`

**Expected Output**:
```
Noted. I've removed this task:
 [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list.
```

#### Find Tasks
Search for tasks containing specific keywords.

**Command**: `find <keyword>`

**Example**: `find book`

**Expected Output**:
```
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 15 2025)
```

### Exiting Atlas

**Command**: `bye`

**Expected Output**:
```
Bye. Hope to see you again soon!
```

The application will close automatically after saying goodbye.

## Task Types and Formatting

### Task Status Icons
- `[ ]` - Task not completed
- `[X]` - Task completed

### Task Type Icons
- `[T]` - Todo task
- `[D]` - Deadline task  
- `[E]` - Event task

### Example Task Display
- Todo: `[T][ ] read book`
- Deadline: `[D][X] return book (by: Oct 15 2025)`
- Event: `[E][ ] project meeting (from: Mon 2pm to: 4pm)`

## Visual Features

### Color-Coded Dialog Boxes
Atlas uses different colored dialog boxes to help you quickly identify the type of action:

- **Yellow**: Adding new tasks (todo, deadline, event)
- **Green**: Marking/unmarking tasks
- **Pink**: Deleting tasks
- **Default**: Other commands and responses

### User Interface
- **Chat Area**: Scrollable area showing your conversation with Atlas
- **Text Field**: Where you type your commands
- **Send Button**: Click to submit your command
- **User Avatar**: Shows your messages on the right
- **Atlas Avatar**: Shows Atlas's responses on the left

## Data Persistence

Atlas automatically saves your tasks to a file called `Atlas.txt` in the `data` folder. Your tasks will be restored the next time you start the application.

## Tips for Using Atlas

1. **Use clear descriptions**: Write descriptive task names to make them easy to find later
2. **Use the find command**: When you have many tasks, use `find` to quickly locate specific ones
3. **Check your list regularly**: Use the `list` command to see all your tasks
4. **Mark completed tasks**: Keep your task list organized by marking completed items
5. **Use proper date format**: For deadlines, always use yyyy-MM-dd format (e.g., 2025-12-25)

## Error Handling

Atlas will show friendly error messages if you:
- Use an invalid command
- Provide missing or invalid arguments
- Try to mark/delete non-existent tasks
- Use an invalid date format for deadlines
- Try to add duplicate tasks

## Getting Help

If you're unsure about a command, just try typing it! Atlas will provide helpful error messages to guide you in the right direction.

---

**Happy task managing with Atlas!** 🗺️