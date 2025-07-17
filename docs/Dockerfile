#FROM nginx:alpine AS runtime
#COPY ./nginx.conf /etc/nginx/nginx.conf
#COPY ./dist /usr/share/nginx/html
#CMD ["nginx", "-g", "daemon off;"]

FROM httpd:latest AS runtime
COPY ./dist /usr/local/apache2/htdocs/