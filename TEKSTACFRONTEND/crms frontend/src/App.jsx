// src/App.jsx

import { useState, useEffect } from 'react';
import Login from './components/Login';
import Vehicles from './components/Vehicles';
import Booking from './components/Booking';
import Confirmation from './components/Confirmation';
import Navbar from './components/Navbar';
import Dashboard from './components/Dashboard';
import MyBookings from './components/MyBookings';

// Import all necessary CSS files
import './App.css';
import './components/BookingManagement.css';
import './components/MyBookings.css'; 

function App() {
    const [view, setView] = useState('login');
    const [isDarkMode, setIsDarkMode] = useState(false);
    const [userRole, setUserRole] = useState(null);
    const [selectedVehicle, setSelectedVehicle] = useState(null);
    const [bookingDetails, setBookingDetails] = useState(null);
    const [dashboardTab, setDashboardTab] = useState('cars');

    useEffect(() => {
        const token = localStorage.getItem('token');
        const role = localStorage.getItem('role');
        if (token && role) {
            handleAuthSuccess(role, token);
        }
    }, []);

    const handleAuthSuccess = (role, token) => {
        localStorage.setItem('token', token);
        localStorage.setItem('role', role);
        setUserRole(role);
        setView(role === 'ADMIN' ? 'dashboard' : 'vehicles');
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        setUserRole(null);
        setView('login');
    };
    
    const handleSelectVehicle = (vehicle) => {
        setSelectedVehicle(vehicle);
        setView('booking');
    };

    const handleConfirmBooking = (details) => {
        setBookingDetails(details);
        setView('confirmation');
    };
    
    const handleReturnToVehicles = () => {
        setSelectedVehicle(null);
        setView('vehicles');
    }

    const handleReturnHome = () => {
        setView('vehicles');
        setSelectedVehicle(null);
        setBookingDetails(null);
    }

    const toggleTheme = () => setIsDarkMode(!isDarkMode);

     const handleDashboardNav = (tab) => {
        setDashboardTab(tab);
        setView('dashboard');
    };

    if (!userRole) {
        return <Login onAuthSuccess={handleAuthSuccess} isDarkMode={isDarkMode} setIsDarkMode={setIsDarkMode} />;
    }
    
    return (
         <div className={`app-container ${isDarkMode ? 'dark' : ''}`}>
           <Navbar 
                userRole={userRole} 
                onLogout={handleLogout} 
                isDarkMode={isDarkMode} 
                toggleTheme={toggleTheme} 
                setView={setView}
                onDashboardNavigate={handleDashboardNav}
                view={view} // <-- Pass the current view
                dashboardTab={dashboardTab} // <-- Pass the current dashboard tab
            />
            <main className="content-container">
                {/* THE FIX IS HERE: Pass the initialTab prop to the Dashboard */}
                {view === 'dashboard' && <Dashboard isDarkMode={isDarkMode} initialTab={dashboardTab} />}
                
                {view === 'vehicles' && <Vehicles onSelectVehicle={handleSelectVehicle} isDarkMode={isDarkMode} />}
                {view === 'booking' && <Booking vehicle={selectedVehicle} onConfirmBooking={handleConfirmBooking} onGoBack={handleReturnToVehicles} isDarkMode={isDarkMode} />}
                {view === 'confirmation' && <Confirmation bookingDetails={bookingDetails} onReturnHome={handleReturnHome} isDarkMode={isDarkMode} />}
                {view === 'my-bookings' && <MyBookings isDarkMode={isDarkMode} />}
            </main>
        </div>
    );
}

export default App;