import { Component, OnInit } from '@angular/core';
import { ChatService } from '../_services/chat.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private chatService: ChatService, private router: Router) {
    this.chatService.fetch.subscribe(v => {
      if(v == 0) this.router.navigateByUrl("/loading")
      if(v == 100) this.router.navigateByUrl("/chat")
    })
   }

  ngOnInit(): void {

  }

}
