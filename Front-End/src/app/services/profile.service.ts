import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { SessionStorageService } from 'ngx-webstorage';
import { User } from '../entities/user';
import * as h from '../host'; 

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http:HttpClient,private sessionSt:SessionStorageService) { }

  getUser(email){
    const httpOptions = {
        headers: new HttpHeaders({
          'Content-Type':  'application/json',
          'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
        }),
        params: new HttpParams().set('email',email)
      };
      return this.http.get(h.host+'/user',httpOptions);
  }

  addFriend(email){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
    };
    return this.http.post(h.host+'/addFriend',email,httpOptions);
  }

  getFriendshipStatus(email){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('email',email),
      responseType: 'text' as 'text'//due to angular bug: https://github.com/angular/angular/issues/18586
    };
    return this.http.get(h.host+'/friendshipStatus',httpOptions);
  }

  editProfile(name,phone,workExp,eduExp,skillsExp,user: User,fd){
    const httpOptions = {
      headers: new HttpHeaders({
        //'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
        'workExp': workExp,
        'eduExp': eduExp,
        'skillsExp': skillsExp,
      }),
      params: new HttpParams().set('name',name).set('phone',phone).set('workPrivacy',user.workPrivacy).set('eduPrivacy',user.eduPrivacy).set('skillsPrivacy',user.skillsPrivacy),
    };
    return this.http.post(h.host+'/editProfile',fd,httpOptions);
  }
}