// src/components/Navbar.jsx
import React from 'react';
import PillNav from './PillNav'; // Import the new PillNav component
import './Navbar.css';

const Navbar = ({
    userRole,
    onLogout,
    isDarkMode,
    toggleTheme,
    setView,
    onDashboardNavigate,
    view, // We need the current view to set the active state
    dashboardTab // We need the current dashboard tab as well
}) => {
    
    // Define the navigation items for a regular user
    const userItems = [
        { label: 'Find a Car', href: 'vehicles' },
        { label: 'My Bookings', href: 'my-bookings' },
    ];
    
    // Define the navigation items for an admin
    const adminItems = [
        { label: 'Manage Cars', href: 'cars' },
        { label: 'View Users', href: 'users' },
        { label: 'View Bookings', href: 'bookings' },
    ];
    
    // Choose which set of items to display based on user role
    const navItems = userRole === 'ADMIN' ? adminItems : userItems;
    
    // Determine the currently active navigation link
    // For admins, the active link is the current tab. For users, it's the current view.
    const activeHref = userRole === 'ADMIN' ? dashboardTab : view;

    // The navigation handler function for the PillNav
    const handleNavigation = (href) => {
        if (userRole === 'ADMIN') {
            onDashboardNavigate(href);
        } else {
            setView(href);
        }
    };

    const SunIcon = () => <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="12" r="4"/><path d="M12 2v2"/><path d="M12 20v2"/><path d="m4.93 4.93 1.41 1.41"/><path d="m17.66 17.66 1.41 1.41"/><path d="M2 12h2"/><path d="M20 12h2"/><path d="m6.34 17.66-1.41 1.41"/><path d="m19.07 4.93-1.41 1.41"/></svg>;
    const MoonIcon = () => <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 3a6 6 0 0 0 9 9 9 9 0 1 1-9-9Z"/></svg>;

    return (
        <header className={`navbar-header ${isDarkMode ? 'dark' : ''}`}>
            <div className="navbar-brand">
                <a href="#" onClick={(e) => { e.preventDefault(); handleNavigation(userRole === 'ADMIN' ? 'cars' : 'vehicles'); }}>
                    RentalWheels
                </a>
            </div>
            
            <PillNav
                items={navItems}
                activeHref={activeHref}
                onNavigate={handleNavigation}
                // Dynamically set colors based on the theme
                baseColor={isDarkMode ? "#E0E7FF" : "#1F2937"}
                pillColor={isDarkMode ? "#1F2937" : "#FFFFFF"}
                hoveredPillTextColor={isDarkMode ? "#1F2937" : "#FFFFFF"}
                pillTextColor={isDarkMode ? "#E0E7FF" : "#1F2937"}
            />
            
            <div className="navbar-actions">
                <button className="theme-toggle-btn-nav" onClick={toggleTheme} title="Toggle Theme">
                    {isDarkMode ? <SunIcon /> : <MoonIcon />}
                </button>
                <button onClick={onLogout} className="logout-btn-nav">Logout</button>
            </div>
        </header>
    );
};

export default Navbar;