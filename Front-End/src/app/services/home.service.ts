import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders,HttpParams } from '@angular/common/http';
import { SessionStorageService } from 'ngx-webstorage';
import * as h from '../host'; 

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(private http:HttpClient,private sessionSt:SessionStorageService) { }

  post(postText,fd){
    const httpOptions = {
      headers: new HttpHeaders({
        // 'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('postText',postText),
    };
    return this.http.post<boolean>(h.host+'/post',fd,httpOptions);
  }

  getArticles(){
    console.log("getting articles...");
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
    };
    return this.http.get(h.host+'/articles',httpOptions);
  }

  comment(articleId,commentText){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('articleId',articleId).set('commentText',commentText),

    };
    return this.http.post<boolean>(h.host+'/comment',null,httpOptions);
  }

  getComments(articleId){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('articleId',articleId)

    };
    return this.http.get(h.host+'/getComments',httpOptions);
  }

  like(articleId,status){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('articleId',articleId).set('status',status),

    };
    return this.http.post<boolean>(h.host+'/like',null,httpOptions);
  }

  getLike(articleId){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('articleId',articleId)

    };
    return this.http.get(h.host+'/getLike',httpOptions);
  }
}
