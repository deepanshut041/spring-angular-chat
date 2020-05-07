import { UserMessage } from './UserMessage'

export class NbMessage{
    date: Date
    files = []
    message: string
    quote:	string = ""
    sender:	string
    type: string
    avatar:	string = ""
    reply:	boolean
    latitude: number = 0
    longitude: number = 0

    constructor(msg: UserMessage){
        this.date = msg.createdAt
        this.message = msg.content
        if(msg.contentType != 'TEXT'){
            this.type = "file"
            this.files = [{"url": msg.mediaUrl, icon: 'file-outline'}]
        } else{
            this.type = "text"
        }
    }

    updateUser(name, imgUrl, reply){
        this.reply = reply
        this.avatar = imgUrl
        this.sender = name
    }
}