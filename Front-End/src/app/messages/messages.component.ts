import { Component, OnInit, ViewChild } from '@angular/core';
import { SessionStorageService } from 'ngx-webstorage';
import { NetworkService } from '../services/network.service';
import { User } from '../entities/user';
import { ActivatedRoute } from '@angular/router';
import { MessagesService } from '../services/messages.service';
import { NgForm } from '@angular/forms';
import { Chat } from '../entities/chat';
import { Message } from '../entities/message';
import { ProfileService } from '../services/profile.service';
import * as h from '../host'; 

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {
  @ViewChild('f') message: NgForm;

  friends: User[] = [];
  email: string;
  mymessages: Message[] = [];
  me: User;
  friend: User;
  host = h.host;

  constructor(private messages:MessagesService,private route: ActivatedRoute,private sessionSt:SessionStorageService,private network:NetworkService, private profile:ProfileService) { }

  ngOnInit() {
    this.profile.getUser(this.sessionSt.retrieve('email')).subscribe(
      (user: User)=>{
        this.me = user;
      });

    this.email = this.route.snapshot.paramMap.get('email');

    this.messages.getChatUsers().subscribe(
      (users: any[]) => {
        console.log(users);
        for(var i in users){
          this.friends[i] = users[i];
        }

        if(this.email === null){
          this.email = this.friends[0].email;
          this.messages.getChat(this.email).subscribe(
            (chat: Message[])=>{
              this.profile.getUser(this.email).subscribe(
                (user: User)=>{
                  this.friend = user;
                });
              for(var i in chat){
                this.mymessages[i] = chat[i];
              }
            }
          );
        }
        else{
          this.messages.getChat(this.email).subscribe(
            (chat: Message[])=>{
              this.profile.getUser(this.email).subscribe(
                (user: User)=>{
                  this.friend = user;
                });
              for(var i in chat){
                this.mymessages[i] = chat[i];
              }
            }
          );
        }
      }
    );
  }

  onSend(){
    this.messages.sendMessage(this.email,this.message.value.messageText).subscribe();
  }

}
