import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'home-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss']
})
export class ChatListComponent implements OnInit {
  users: any[]

  constructor() { }

  ngOnInit(): void {
    this.dummyUsers()
  }

  dummyUsers(){
    let users = []
    for (let i = 0; i < 20; i++) {
      users.push(
        {
          "name": `User ${i}`,
          "title": `Sample title ${i}`
        }
      )
    this.users = users
    }
  }

}
