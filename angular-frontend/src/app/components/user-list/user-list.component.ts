import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UserDto } from 'src/app/dto/user-dto';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  constructor(private userService: UserService) { }

  users: Array<UserDto> = []

  ngOnInit(): void {
    this.fetchData()
  }

  delete(id: string) {
    this.userService.deleteUser(id).subscribe(() => {
      this.fetchData()
    })
  }

  private fetchData() {
    this.userService.getUsers().subscribe(data => {
      this.users = data
      console.log(this.users)
    })
  }

}
