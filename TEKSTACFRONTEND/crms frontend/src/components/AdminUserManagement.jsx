// src/components/AdminUserManagement.jsx
import React, { useState, useEffect } from 'react';
import { getAllUsers } from '../services/adminService';
import './AdminUserManagement.css'; // We will create this file next

const AdminUserManagement = ({ isDarkMode }) => {
    const [users, setUsers] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchUsers = async () => {
            setIsLoading(true);
            setError('');
            try {
                const data = await getAllUsers();
                setUsers(data);
            } catch (err) {
                setError('Failed to fetch users. Please make sure you are logged in as an admin.');
                console.error(err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchUsers();
    }, []);

    return (
        <div className={`admin-user-management ${isDarkMode ? 'dark' : ''}`}>
            <h2>User Management</h2>
            
            {isLoading && <p>Loading users...</p>}
            {error && <p className="error-message">{error}</p>}

            <div className="user-table-container">
                <table>
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Date Joined</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.length > 0 ? (
                            users.map((user) => (
                                <tr key={user.userId}>
                                    <td>{user.userId}</td>
                                    <td>{user.firstName}</td>
                                    <td>{user.lastName}</td>
                                    <td>{user.email}</td>
                                    <td>{user.phone}</td>
                                    <td>{new Date(user.createdDate).toLocaleDateString()}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="6">No users found.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default AdminUserManagement;