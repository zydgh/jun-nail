FROM node:22-alpine AS build
WORKDIR /web
COPY plus-ui/package.json plus-ui/package.json
WORKDIR /web/plus-ui
RUN npm install --registry=https://registry.npmmirror.com
COPY plus-ui/ /web/plus-ui/
RUN npm run build:prod

FROM nginx:1.27-alpine
COPY docker/nginx.conf /etc/nginx/nginx.conf
COPY --from=build /web/plus-ui/dist /usr/share/nginx/html
EXPOSE 80
