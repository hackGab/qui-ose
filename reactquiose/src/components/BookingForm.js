import React, { Component } from 'react';

class BookingForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            email: '',
            date: '',
            guests: 1,
            specialRequests: '',
            submitted: false,
        };
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };

    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({ submitted: true });
    };

    render() {
        const { name, email, date, guests, specialRequests, submitted } = this.state;

        if (submitted) {
            return (
                <div>
                    <h1>Booking Confirmed</h1>
                    <p>Name: {name}</p>
                    <p>Email: {email}</p>
                    <p>Date: {date}</p>
                    <p>Guests: {guests}</p>
                    <p>Special Requests: {specialRequests}</p>
                </div>
            );
        }

        return (
            <form onSubmit={this.handleSubmit}>
                <h1>Booking Form</h1>
                <div>
                    <label>Name:</label>
                    <input
                        type="text"
                        name="name"
                        value={name}
                        onChange={this.handleInputChange}
                    />
                </div>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        name="email"
                        value={email}
                        onChange={this.handleInputChange}
                    />
                </div>
                <div>
                    <label>Date:</label>
                    <input
                        type="date"
                        name="date"
                        value={date}
                        onChange={this.handleInputChange}
                    />
                </div>
                <div>
                    <label>Guests:</label>
                    <input
                        type="number"
                        name="guests"
                        value={guests}
                        onChange={this.handleInputChange}
                        min="1"
                        max="10"
                    />
                </div>
                <div>
                    <label>Special Requests:</label>
                    <textarea
                        name="specialRequests"
                        value={specialRequests}
                        onChange={this.handleInputChange}
                    ></textarea>
                </div>
                <button type="submit">Submit</button>
            </form>
        );
    }
}

export default BookingForm;
