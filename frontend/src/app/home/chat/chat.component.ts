import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'home-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  messages: any[]
  constructor() { }

  ngOnInit(): void {
  }

}
