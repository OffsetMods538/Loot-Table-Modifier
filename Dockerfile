#FROM nginx:alpine AS runtime
#COPY ./nginx.conf /etc/nginx/nginx.conf
#COPY ./dist /usr/share/nginx/html
#CMD ["nginx", "-g", "daemon off;"]

#FROM httpd:latest AS runtime
#COPY ./dist /usr/local/apache2/htdocs/

FROM node:lts AS build
WORKDIR /docs
COPY ./docs .
RUN npm i
RUN npm run build

FROM httpd:2.4 AS runtime
COPY --from=build /docs/dist /usr/local/apache2/htdocs/
