export class UserMessage {
    id: string
    senderId: string
    chatId: string
    content: string
    files: any[]
    contentType: string
    createdAt: Date
    updatedAt: Date
    read: boolean

    constructor(id: string, senderId: string, chatId: string, content: string, files: any[], contentType: string,
        createdAt: string, updatedAt: string, read: boolean) {
        this.id = id
        this.senderId = senderId
        this.chatId = chatId
        this.content = content
        this.files = files
        this.contentType = contentType
        this.createdAt = new Date(createdAt)
        this.updatedAt = new Date(updatedAt)
        this.read = read
    }
}