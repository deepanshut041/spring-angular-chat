export class UserProfile {
    id: string
    email: string
    name: string
    imgUrl: string

    constructor(id: string, email: string, name: string, imgUrl: string){
        this.email = email
        this.name = name
        this.imgUrl = imgUrl
        this.id = id
    }
}