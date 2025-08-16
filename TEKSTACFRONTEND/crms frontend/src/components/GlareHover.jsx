import "./GlareHover.css";

const GlareHover = ({
    children,
    glareColor = "#ffffff",
    glareOpacity = 0.5,
    glareAngle = -45,
    glareSize = 250,
    transitionDuration = 650,
    className = "",
    style = {},
}) => {
    // This logic converts hex color to rgba to include opacity
    const hex = glareColor.replace("#", "");
    let rgba = `rgba(255, 255, 255, ${glareOpacity})`;
    if (/^[0-9A-Fa-f]{6}$/.test(hex)) {
        const r = parseInt(hex.slice(0, 2), 16);
        const g = parseInt(hex.slice(2, 4), 16);
        const b = parseInt(hex.slice(4, 6), 16);
        rgba = `rgba(${r}, ${g}, ${b}, ${glareOpacity})`;
    }

    // CSS variables to control the effect from props
    const vars = {
        "--gh-angle": `${glareAngle}deg`,
        "--gh-duration": `${transitionDuration}ms`,
        "--gh-size": `${glareSize}%`,
        "--gh-rgba": rgba,
    };

    return (
        <div className={`glare-hover ${className}`} style={{ ...vars, ...style }}>
            {children}
        </div>
    );
};

export default GlareHover;