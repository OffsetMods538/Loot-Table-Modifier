#FROM nginx:alpine AS runtime
#COPY ./nginx.conf /etc/nginx/nginx.conf
#COPY ./dist /usr/share/nginx/html
#CMD ["nginx", "-g", "daemon off;"]

#FROM httpd:latest AS runtime
#COPY ./dist /usr/local/apache2/htdocs/

FROM node:lts-slim AS base
ENV PNPM_HOME="/pnpm"
ENV PATH="$PATH:$PNPM_HOME"
RUN corepack enable
COPY /docs /docs
WORKDIR /docs

FROM base AS build
RUN --mount=type=cache,id=pnpm,target=/pnpm/store pnpm install --frozen-lockfile
RUN pnpm run build

FROM httpd:alpine
COPY --from=build /docs/dist /usr/local/apache2/htdocs/
EXPOSE 80


#FROM base
#COPY --from=prod-deps
#COPY package.json pnpm-lock.yaml
#RUN npm install -g pnpm



#FROM node:lts AS setup
#WORKDIR /docs
#COPY ./docs .
#RUN npm install -g pnpm
#RUN pnpm i
#RUN pnpm run build

#FROM httpd:2.4 AS runtime
#COPY --from=build /docs/dist /usr/local/apache2/htdocs/
