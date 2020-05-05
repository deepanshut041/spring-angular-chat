import { Component, OnInit } from '@angular/core';
import { NbMenuService } from '@nebular/theme';
import { filter, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { UserService } from 'src/app/_services/user.service';
import { UserProfile } from 'src/app/_dtos/user/UserProfile';

@Component({
  selector: 'home-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss']
})
export class ChatListComponent implements OnInit {

  chatList: any[] = []
  menu = [
    { title: 'Profile', icon: 'person-outline' },
    { title: 'New Chat', icon: 'person-add-outline' },
    { title: 'New Group', icon: 'plus-outline' },
    { title: 'Settings', icon: 'settings-outline' },
    { title: 'Log out', icon: 'unlock-outline' },
  ];

  profile: UserProfile

  constructor(private menuService: NbMenuService, private router: Router, private userService: UserService) {
    this.profile = this.userService.getProfile()
  }

  ngOnInit(): void {
    this.dummyUsers()
    this.menuListener()
  }

  menuListener() {
    this.menuService.onItemClick()
      .pipe(
        filter(({ tag }) => tag === 'context-chat-more'),
        map(({ item: { title } }) => title),
      )
      .subscribe(title => {
        switch (title) {
          case 'Profile':
            this.router.navigateByUrl("/profile")
            break;
          case 'New Chat':

            break;
          case 'New Group':

            break;
          case 'Settings':
            this.router.navigateByUrl("/settings")
            break;
          case 'Log out':
            this.userService.logout()
            this.router.navigateByUrl("/auth")
            break;
          default:
            break;
        }
      })
  }

  dummyUsers() {
    let users = []
    for (let i = 0; i < 20; i++) {
      users.push(
        {
          "name": `User ${i}`,
          "title": `Sample title ${i}`
        }
      )
      this.chatList = users
    }
  }

}
