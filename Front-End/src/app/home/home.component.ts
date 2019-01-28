import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HomeService } from '../services/home.service';
import { Article } from '../entities/article';
import { SessionStorageService } from 'ngx-webstorage';
import { ProfileService } from '../services/profile.service';
import { User } from '../entities/user';
import { MyLike } from '../entities/mylike';
import { Comment } from '../entities/comment';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/forkJoin';
import * as h from '../host'; 
import { query } from '@angular/core/src/render3/query';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})


export class HomeComponent implements OnInit {
  @ViewChild('f') post: NgForm;

  host = h.host;
  file = null;
  mypostswitch=false;

  commentForPost: string[] = [];

  user: User;

  constructor(private home:HomeService,private sessionSt:SessionStorageService,private profile:ProfileService) { }

  articles: Article[] = [];
  myarticles: Article[] = [];
  otherarticles: Article[] = [];
  commentsArray: Comment[][] = [];
  likesArray: MyLike[] = [];
  commentText: string;

  ngOnInit() {    
    this.profile.getUser(this.sessionSt.retrieve('email')).subscribe(
      (user:User)=>{
        console.log(user);
        this.user = user;
      }
    )
    this.home.getArticles().subscribe(
      (articles: any[]) => {
        console.log(articles);
        let observables: Observable<any>[] = [];
        for (let i = 0; i < articles.length; i++) {
            observables.push(this.home.getComments(articles[i].id))
        }
        Observable.forkJoin(observables)
            .subscribe((dataArray: any[]) => {
                this.commentsArray = dataArray;
                for(var i in articles){
                  this.articles[i] = articles[i];
                  this.articles[i].comments = this.commentsArray[i];
                }

                let observables2: Observable<any>[] = [];
                for (let i = 0; i < articles.length; i++) {
                    observables2.push(this.home.getLike(articles[i].id))
                }
                Observable.forkJoin(observables2)
                  .subscribe((dataArray: any[]) => {
                      this.likesArray = dataArray;
                      for(var i in articles){
                        if(this.likesArray[i]!=null){
                          console.log("Like status: ",this.likesArray[i].status);
                          this.articles[i].like = this.likesArray[i];
                          if(this.articles[i].like.status == "Like"){
                            this.articles[i].like.status = "Dislike";
                          }
                        }
                        else{
                          console.log("Like status: null");
                          this.articles[i].like = new MyLike();
                          this.articles[i].like.status = "Like";
                        }
                      }
                  });
                  for(var i in this.articles)
                  {
                    if(this.articles[i].user.email==this.sessionSt.retrieve('email'))
                      this.myarticles.push(this.articles[i]);
                    else
                      this.otherarticles.push(this.articles[i]);
                  } 
            });
      }
    );
  }

  onSubmit(){
    console.log(this.post);
    console.log(this.post.value.postText);
    const fd = new FormData();
    if(this.file != null){
      fd.append('file',this.file,this.file.name);
    }
    this.home.post(this.post.value.postText,fd).subscribe();
  }

  onPost(articleId,commentText){//Post a Comment
    console.log("articleId: "+articleId+" text: "+commentText);
    this.home.comment(articleId,commentText).subscribe();
  }

  onLike(articleId,status){
    console.log("articleId: "+articleId+" status: "+status);
    this.home.like(articleId,status).subscribe();
    for(var i in this.articles){
      if(this.articles[i].id == articleId){
        if(this.articles[i].like.status === "Dislike"){
          this.articles[i].like.status = "Like";
        }
        else{
          this.articles[i].like.status = "Dislike";
        } 
      }
    }
  }

  onFileSelected(event){
    console.log(event);
    this.file = event.target.files[0];
  }

}
