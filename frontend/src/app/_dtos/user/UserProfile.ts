export class UserProfile {
    email: string
    name: string
    imgUrl: string

    constructor(email: string, name: string, imgUrl: string){
        this.email = email
        this.name = name
        this.imgUrl = imgUrl
    }
}