# Spring Angular Chat(STOMP)

![Cover Image](./images/cover.gif)
1-1 instant messaging project designed to demonstrate WebSockets in a load-balanced environment. Users can register, login/logout, see a friendslist, private message all in realtime. WebSocket usages include user presence monitoring, notifications, and chat messages.

## Technologies/Design Decisions

- Backend: Kotlin with Spring Boot
- Frontend: Angular 8
- Message Broker: RabbitMQ (PubSub pattern for multi-server messaging)
- Database: MongoDB
- ORM: Spring Data
- WebSocket messaging protocol: Stomp
- WebSocket handler: Sock.js (with cross-browser fallbacks)
- Security: Spring Security
- Spring Controllers couple REST as well as WebSocket traffic
- Solid Design Principals.

## Features

- OAUTH2 with Google and Facebook. Users can also register via email.
- Multiple color themes available.
- Private Friends list with blocking unwanted users.
- Messages are persisted. Pwa is available provide desktop application features.
- Offline message support and sync when user is online.
- Easy add new friends via email. Like WhatsApp add via Phone number.
- Chat support Images, Audio, Video, Gif's, Map Location. Multiple files with drop in feature.

## Themes

### Default Theme

![Light Theme](./images/chat.png)

### Dark Theme

![Light Theme](./images/theme-2.png)

### Light Theme

![Light Theme](./images/theme-3.png)

## Screenshots

- [Signin Screen](./images/signin.png)
- [Signup Screen](./images/signin.png)
- [Oauth2 Confirmation](./images/token.png)
- [Home Screen](./images/home.png)
- [Chat](./images/chat.png)
- [Add New Friend](./images/new_friend.png)
- [Edit Profile](./images.edit-profile.png)

## Any questions

If you have any questions, feel free to ask me:

- **Mail**: <a href="mailto:deepanshut041@gmail.com">deepanshut041@gmail.com</a>  
- **Github**: [https://github.com/data-breach/MlAgents](https://github.com/deepanshut041/friends-chat)
- **Website**: [https://data-breach.github.io/MlAgents](https://deepanshut041.github.io/friends-chat)
- **Twitter**: <a href="https://twitter.com/deepanshut041">@deepanshut041</a>

Don't forget to follow me on <a href="https://twitter.com/deepanshut041">twitter</a>, <a href="https://github.com/deepanshut041">github</a> and <a href="https://medium.com/@deepanshut041">Medium</a> to be alerted of the new articles that I publish
