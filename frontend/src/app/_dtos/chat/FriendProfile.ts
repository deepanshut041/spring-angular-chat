export class FriendProfile {
    id: string
    email: string
    name: string
    imgUrl: string
    isBlocked: boolean
    blockedBy: string
    lastMsg: string = ""
    lastMsgAt: Date
    unreadMsgs: number = 0
    updatedAt: Date
    fetched: boolean = false

    constructor(id: string, email: string, name: string, imgUrl: string, isBlocked: boolean, blockedBy: string, updatedAt: string) {
        this.id = id
        this.email = email
        this.name = name
        this.imgUrl = imgUrl
        this.isBlocked = isBlocked
        this.blockedBy = blockedBy
        this.updatedAt = new Date(updatedAt)
    }

    updateConv(lastMsg: string, lastMsgAt: Date) {
        this.lastMsg = lastMsg
        this.lastMsgAt = lastMsgAt
    }

    update(id: string, email: string, name: string, imgUrl: string, isBlocked: boolean, blockedBy: string, updatedAt: Date){
        this.id = id
        this.email = email
        this.name = name
        this.imgUrl = imgUrl
        this.isBlocked = isBlocked
        this.blockedBy = blockedBy
        this.updatedAt = updatedAt
    }

    incrementUnread(){
        this.unreadMsgs += 1
    }
}