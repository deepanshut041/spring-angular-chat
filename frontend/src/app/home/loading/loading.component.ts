import { Component, OnInit } from '@angular/core';
import { ChatService } from 'src/app/_services/chat.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.scss']
})
export class LoadingComponent implements OnInit {

  constructor(private chatService: ChatService, private router: Router) { }

  ngOnInit(): void {
    this.chatService.fetchFriends().subscribe((output)=>{
      this.chatService.updateFetch(10)
      this.chatService.fetchAllMessages().subscribe(v =>{
        this.chatService.updateFetch(100)
      })
    })
  }

}
