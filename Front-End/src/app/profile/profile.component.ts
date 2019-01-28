import { Component, OnInit, ViewChild } from '@angular/core';
import { Router,ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { SessionStorageService } from 'ngx-webstorage';
import { ProfileService } from '../services/profile.service';
import { User } from '../entities/user';
import { NotificationsService } from '../services/notifications.service';
import * as h from '../host'; 

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  @ViewChild('f') form: NgForm;

  displaySwitch = true;
  myprofile = false;
  user: User;
  fullname: string;
  friendStatus = "Add Friend";
  adminSwitch = false;
  file = null;
  email: string;
  host = h.host;

  constructor(private router:Router,private route: ActivatedRoute,private sessionSt:SessionStorageService,private profile:ProfileService,private notifications:NotificationsService) { }

  ngOnInit() {
    var email = this.route.snapshot.paramMap.get('email');
    this.email = email;
    if(this.sessionSt.retrieve('email') === 'admin')
    {
      this.profile.getUser(email).subscribe(
        (user:User)=>{
          console.log(user);
          this.user = user;
        }
      );
      this.adminSwitch=true;
      return;
    }
    if(email === 'me' || email === this.sessionSt.retrieve('email')){
      this.myprofile = true;
      this.profile.getUser(this.sessionSt.retrieve('email')).subscribe(
        (user:User)=>{
          console.log(user);
          this.user = user;
          this.fullname = user.firstname + " " + user.lastname;
        }
      )
    }
    else{
      this.profile.getUser(email).subscribe(
        (user:User)=>{
          console.log(user);
          this.user = user;
        }
      );
      this.profile.getFriendshipStatus(email).subscribe(
        status=>{
          if(status === "Pending" || status === "Friends"){
            this.friendStatus = "Remove Friend";  
          }
        },
        error =>{
          console.log(error);
        }
      );
    }
  }

  onClick(){
    if(this.friendStatus === "Add Friend"){
      this.profile.addFriend(this.user.email).subscribe();
      this.friendStatus = "Remove Friend";
    }
    else if(this.friendStatus === "Remove Friend"){
      this.notifications.Decline(this.user.email).subscribe();
      this.friendStatus = "Add Friend";
    }
  }

  editProf(){
    this.displaySwitch = false;
  }

  saveChanges(){
    var count = 0;
    for(var i in this.form.value.name){
      if(this.form.value.name[i] === " "){
        count++;
      }
    }
    if(count != 1){
      window.alert("Invalid name!!!");
      console.log("count = " + count);
    }
    else{
      const fd = new FormData();
      if(this.file != null){
        fd.append('file',this.file,this.file.name);
        console.log("filename: "+this.file.name);
      }
      this.profile.editProfile(this.form.value.name,this.form.value.phone,this.form.value.workExp,this.form.value.eduExp,this.form.value.skillsExp,this.user,fd).subscribe(
        (res)=>{
          this.profile.getUser(this.user.email).subscribe(
            (user:User)=>{
              console.log(user);
              this.user = user;
              this.fullname = user.firstname + " " + user.lastname;
            }
          );
        }
      );
      this.displaySwitch = true;
    }
    
  }

  onFileSelected(event){
    console.log(event);
    this.file = event.target.files[0];
  }

  onPrivacyChange(event){
    console.log(event);
    if(event.target.name === "workPrivacy"){
      this.user.workPrivacy = event.target.value;
    }
    else if(event.target.name === "eduPrivacy"){
      this.user.eduPrivacy = event.target.value;
    }
    else if(event.target.name === "skillsPrivacy"){
      this.user.skillsPrivacy = event.target.value;
    }
  }

}
