import { Component, OnInit } from '@angular/core';
import { NotificationService } from 'src/app/_services/notification.service';
import { DataService } from 'src/app/_services/data.service';
import { ChatService } from 'src/app/_services/chat.service';
import { Router } from '@angular/router';

@Component({
  selector: 'home-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  constructor(private notificationService: NotificationService, private chatService: ChatService, private router: Router) { }

  ngOnInit(): void {
    this.notificationService.suscribe()
  }

}
