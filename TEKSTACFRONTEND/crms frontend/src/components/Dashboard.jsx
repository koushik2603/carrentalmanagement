// src/components/Dashboard.jsx
import React, { useState, useEffect } from 'react'; // Import useEffect
import AdminCarManagement from './AdminCarManagement';
import UserManagement from './UserManagement';
import BookingManagement from './BookingManagement';
import './Dashboard.css';

// Accept a new prop `initialTab`
const Dashboard = ({ isDarkMode, initialTab }) => {
    // Initialize the active tab state with the prop, defaulting to 'cars'
    const [activeTab, setActiveTab] = useState(initialTab || 'cars');

    // This effect ensures the tab updates if you click a navbar link while already on the dashboard
    useEffect(() => {
        setActiveTab(initialTab || 'cars');
    }, [initialTab]);

    return (
        <div className={`dashboard-page ${isDarkMode ? 'dark' : ''}`}>
            <header className="dashboard-header">
                <h1>Admin Dashboard</h1>
            </header>
            
            

            <div className="dashboard-content-area">
                {activeTab === 'cars' && <AdminCarManagement isDarkMode={isDarkMode} />}
                {activeTab === 'users' && <UserManagement isDarkMode={isDarkMode} />}
                {activeTab === 'bookings' && <BookingManagement isDarkMode={isDarkMode} />}
            </div>
        </div>
    );
};

export default Dashboard;