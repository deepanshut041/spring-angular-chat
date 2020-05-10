import { UserMessage } from './UserMessage'

export class NbMessage {
    date: Date
    files = []
    text: string
    quote: string = ""
    sender: string
    type: string
    avatar: string = ""
    reply: boolean
    latitude: number = 0
    longitude: number = 0

    constructor(msg: UserMessage) {
        this.date = msg.createdAt
        this.text = msg.content
        if (msg.contentType == 'FILE') {
            this.type = "file"
            this.files = msg.files
        } else {
            this.type = "text"
        }
    }

    updateUser(name, imgUrl, reply) {
        this.reply = reply
        this.avatar = imgUrl
        this.sender = name
    }
}