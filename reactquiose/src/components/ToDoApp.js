import React, { Component } from 'react';

class ToDoApp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tasks: JSON.parse(localStorage.getItem('tasks')) || [], // Récupérer les tâches du localStorage
            newTask: ''
        };
    }

    componentDidUpdate(prevProps, prevState) {
        if (prevState.tasks !== this.state.tasks) {
            localStorage.setItem('tasks', JSON.stringify(this.state.tasks)); // Sauvegarder les tâches à chaque mise à jour
        }
    }

    handleInputChange = (event) => {
        this.setState({ newTask: event.target.value });
    };

    addTask = () => {
        if (this.state.newTask.trim() === '') return;
        const newTask = { text: this.state.newTask, completed: false }; // Ajout du statut "complétée"
        this.setState((prevState) => ({
            tasks: [...prevState.tasks, newTask],
            newTask: ''
        }));
    };

    toggleTaskCompletion = (index) => {
        this.setState((prevState) => {
            const updatedTasks = prevState.tasks.map((task, i) =>
                i === index ? { ...task, completed: !task.completed } : task
            );
            return { tasks: updatedTasks };
        });
    };

    removeTask = (index) => {
        this.setState((prevState) => ({
            tasks: prevState.tasks.filter((_, i) => i !== index)
        }));
    };

    clearTasks = () => {
        this.setState({ tasks: [] });
    };

    render() {
        const remainingTasks = this.state.tasks.filter(task => !task.completed).length;

        return (
            <div className="todo-app">
                <h1>ToDo List</h1>
                <div>
                    <input
                        type="text"
                        value={this.state.newTask}
                        onChange={this.handleInputChange}
                        placeholder="Add a new task"
                    />
                    <button onClick={this.addTask}>Add Task</button>
                    <button onClick={this.clearTasks} style={{ marginLeft: '10px' }}>Clear All</button>
                </div>
                <p>{remainingTasks} task(s) remaining</p>
                <ul>
                    {this.state.tasks.map((task, index) => (
                        <li key={index} style={{ textDecoration: task.completed ? 'line-through' : 'none' }}>
                            <span onClick={() => this.toggleTaskCompletion(index)}>{task.text}</span>
                            <button onClick={() => this.removeTask(index)} style={{ marginLeft: '10px' }}>Remove</button>
                        </li>
                    ))}
                </ul>
            </div>
        );
    }
}

export default ToDoApp;
