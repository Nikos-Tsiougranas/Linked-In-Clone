import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NetworkService } from '../services/network.service';
import { User } from '../entities/user';
import { ActivatedRoute } from '@angular/router';
import * as h from '../host';

@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.css']
})
export class NetworkComponent implements OnInit {
  @ViewChild('f') search: NgForm;

  displaySwitch = true;
  users: User[] = [];
  host = h.host;

  constructor(private route: ActivatedRoute,private network:NetworkService) { }

  ngOnInit() {
    var email = this.route.snapshot.paramMap.get('email');
    this.network.getNetworkUsers(email).subscribe(
      (users: any[]) => {
        console.log(users);
        for(var i in users){
          console.log(i);
          console.log(users[i]);
          console.log(users[i].firstname+ " " +users[i].lastname);
          this.users[i] = users[i];
        }
      }
    );
  }

  onSubmit(){
    this.users = [];
    console.log(this.search);
    console.log(this.search.value.search);
    this.network.search(this.search.value.search).subscribe(
      (users: any[]) => {
        console.log(users);
        for(var i in users){
          console.log(i);
          console.log(users[i]);
          console.log(users[i].firstname+ " " +users[i].lastname);
          this.users[i] = users[i];
        }
      }
    );
    this.displaySwitch = false;
  }

}
