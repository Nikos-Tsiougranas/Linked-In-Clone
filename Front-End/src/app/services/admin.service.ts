import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { SessionStorageService } from 'ngx-webstorage';
import * as h from '../host'; 

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http:HttpClient,private sessionSt:SessionStorageService) { }
  
  getAllUsers(){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      })
    };
    return this.http.get(h.host+'/getAllUsers',httpOptions);
  }

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
  getXML(values){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/xml',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
      params: new HttpParams().set('values',values),
      responseType: 'blob' as 'blob',
    };
    return this.http.get(h.host+'/getXML',httpOptions);
  } 
}
