// src/components/UserManagement.jsx
import React, { useState, useEffect } from 'react';
import { getRegularUsers } from '../services/userService';
import './UserManagement.css';

const UserManagement = ({ isDarkMode }) => {
    const [users, setUsers] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchUsers = async () => {
            setIsLoading(true);
            setError('');
            try {
                const data = await getRegularUsers();
                setUsers(data);
            } catch (err) {
                setError('Failed to fetch users. Please ensure you are logged in as an admin.');
                console.error(err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchUsers();
    }, []);

    return (
        <div className={`user-management ${isDarkMode ? 'dark' : ''}`}>
            <h2>Registered Users</h2>
            
            {isLoading && <p>Loading users...</p>}
            {error && <p className="error-message">{error}</p>}

            <div className="user-table-container">
                <table>
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Full Name</th> {/* <-- Changed from Name */}
                            <th>Email</th>
                            <th>Contact Number</th> {/* <-- Changed from Phone Number */}
                            <th>Role</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.length > 0 ? (
                            users.map((user) => (
                                <tr key={user.userId}>
                                    <td>{user.userId}</td>
                                    {/* THE FIX: Use fullName */}
                                    <td>{user.fullName}</td>
                                    <td>{user.email}</td>
                                    {/* THE FIX: Use contactNumber */}
                                    <td>{user.contactNumber}</td>
                                    <td><span className="role-badge">{user.userRole}</span></td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="5">No users found.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UserManagement;