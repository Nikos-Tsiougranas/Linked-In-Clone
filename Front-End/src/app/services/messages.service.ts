import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { SessionStorageService } from 'ngx-webstorage';
import * as h from '../host'; 

@Injectable({
  providedIn: 'root'
})
export class MessagesService {

  constructor(private http:HttpClient,private sessionSt:SessionStorageService) { }

  getChatUsers(){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
    };
    return this.http.get(h.host+'/getLatestChats',httpOptions);
  }

  getChat(email){
    const httpOptions = {
        headers: new HttpHeaders({
          'Content-Type':  'application/json',
          'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
        }),
        params: new HttpParams().set('email',email)
      };
      return this.http.get(h.host+'/getChat',httpOptions);
  }

  sendMessage(email,message){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('email',email)
    };
    return this.http.put(h.host+'/sendMessage',message,httpOptions);
  }
}
