export class UserMessage {
    id: string
    senderId: string
    conversationId: string
    content: string
    mediaUrl: string
    contentType: string
    createdAt: Date
    updatedAt: Date
    receivedAt: Date
    readAt: Date

    constructor(id: string, senderId: string, conversationId: string, content: string, mediaUrl: string, contentType: string,
        createdAt: string, updatedAt: string, receivedAt: string, readAt: string) {
        this.id = id
        this.senderId = senderId
        this.conversationId = conversationId
        this.content = content
        this.mediaUrl = mediaUrl
        this.contentType = contentType
        this.createdAt = new Date(createdAt)
        this.updatedAt = new Date(updatedAt)
        this.receivedAt = new Date(receivedAt)
        this.readAt = new Date(readAt)
    }
}