import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { SessionStorageService } from 'ngx-webstorage';
import * as h from '../host'; 

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  constructor(private http:HttpClient,private sessionSt:SessionStorageService) { }

  getFriendRequests(){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      })
    };
    return this.http.get(h.host+'/friendRequests',httpOptions);
  }

  Accept(email){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      })
    };
    return this.http.post(h.host+'/accept',email,httpOptions);
  }

  Decline(email){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      })
    };
    return this.http.post(h.host+'/decline',email,httpOptions);
  }

  getAllComments(){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      })
    };
    return this.http.get(h.host+'/getAllComments',httpOptions);
  }

  getAllLikes(){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      })
    };
    return this.http.get(h.host+'/getAllLikes',httpOptions);
  }

  getArticle(articleId){
    console.log("getting articles...");
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('articleId',articleId)
    };
    return this.http.get(h.host+'/getArticle',httpOptions);
  }
}
