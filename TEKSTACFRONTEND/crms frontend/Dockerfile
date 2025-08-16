# Use a lightweight Nginx web server
FROM nginx:alpine

# Copy the PRE-BUILT 'dist' folder from your project
COPY dist/ /usr/share/nginx/html

# Start the Nginx server
CMD ["nginx", "-g", "daemon off;"]