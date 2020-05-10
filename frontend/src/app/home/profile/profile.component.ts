import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/_services/user.service';
import { UserProfile } from 'src/app/_dtos/user/UserProfile';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  profile: UserProfile

  constructor(private userService: UserService, private router: Router) {
    this.profile = this.userService.getProfile()
  }

  ngOnInit(): void {
  }

  continue(): void{
    this.router.navigateByUrl("/chat")
  }

  uploadFile(file): void{
    console.log(file)
  }

}
