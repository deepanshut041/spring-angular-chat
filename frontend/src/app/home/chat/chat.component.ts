import { Component, OnInit } from '@angular/core';
import { NotificationService } from 'src/app/_services/notification.service';

@Component({
  selector: 'home-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  constructor(private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.notificationService.suscribe()
  }

}
