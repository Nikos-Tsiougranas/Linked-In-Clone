import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { SessionStorageService } from 'ngx-webstorage';
import * as h from '../host';

@Injectable({
  providedIn: 'root'
})
export class NetworkService {

  constructor(private http:HttpClient,private sessionSt:SessionStorageService) { }

  search(name){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('name',name)
    };
    return this.http.get(h.host+'/search',httpOptions);
  }

  getNetworkUsers(email){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('email',email)
    };
    return this.http.get(h.host+'/networkUsers',httpOptions);
  }
}
