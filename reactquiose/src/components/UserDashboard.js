import React, { Component } from 'react';

class UserDashboard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userInfo: {
                name: 'John Doe',
                email: 'john@example.com',
                subscription: 'Premium',
            },
            notifications: ['Welcome to the platform!', 'Your subscription is active.'],
            settings: {
                theme: 'dark',
                notifications: true,
            },
        };
    }

    toggleTheme = () => {
        this.setState((prevState) => ({
            settings: {
                ...prevState.settings,
                theme: prevState.settings.theme === 'dark' ? 'light' : 'dark',
            },
        }));
    };

    toggleNotifications = () => {
        this.setState((prevState) => ({
            settings: {
                ...prevState.settings,
                notifications: !prevState.settings.notifications,
            },
        }));
    };

    changeSubscription = () => {
        this.setState((prevState) => ({
            userInfo: {
                ...prevState.userInfo,
                subscription: prevState.userInfo.subscription === 'Premium' ? 'Basic' : 'Premium',
            },
        }));
    };

    resetSettings = () => {
        this.setState({
            settings: {
                theme: 'dark',
                notifications: true,
            },
        });
    };

    render() {
        const { userInfo, notifications, settings } = this.state;

        return (
            <div className={`dashboard ${settings.theme}`}>
                <h1>User Dashboard</h1>
                <div className="user-info">
                    <p><strong>Name:</strong> {userInfo.name}</p>
                    <p><strong>Email:</strong> {userInfo.email}</p>
                    <p><strong>Subscription:</strong> {userInfo.subscription}</p>
                    <button onClick={this.changeSubscription}>
                        Change Subscription ({userInfo.subscription})
                    </button>
                </div>

                <div className="notifications">
                    <h2>Notifications</h2>
                    {settings.notifications ? (
                        <ul>
                            {notifications.length > 0 ? (
                                notifications.map((notification, index) => (
                                    <li key={index}>{notification}</li>
                                ))
                            ) : (
                                <li>No notifications available</li>
                            )}
                        </ul>
                    ) : (
                        <p>Notifications are disabled</p>
                    )}
                </div>

                <div className="settings">
                    <h2>Settings</h2>
                    <button onClick={this.toggleTheme}>
                        Toggle Theme ({settings.theme})
                    </button>
                    <button onClick={this.toggleNotifications}>
                        {settings.notifications ? 'Disable' : 'Enable'} Notifications
                    </button>
                    <button onClick={this.resetSettings}>
                        Reset Settings
                    </button>
                </div>
            </div>
        );
    }
}

export default UserDashboard;
