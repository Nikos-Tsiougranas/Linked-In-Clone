import { Component, OnInit } from '@angular/core';
import { NotificationsService } from '../services/notifications.service';
import { User } from '../entities/user';
import { Article } from '../entities/article';
import { MyLike } from '../entities/mylike';
import { ActivatedRoute } from '@angular/router';
import { HomeService } from '../services/home.service';
import * as h from '../host'; 

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  users: User[] = [];
  displaySwitch = true;
  articleId: string;
  article: Article;
  commentForPost: string;
  host=h.host;
  allNotifications: any[] = [];

  constructor(private notifications:NotificationsService,private route: ActivatedRoute,private home:HomeService) { }

  ngOnInit() {
    this.articleId = this.route.snapshot.paramMap.get('articleId');
    console.log("this.articleId: ",this.articleId);
    if(this.articleId==null){//regular notification page
      this.notifications.getFriendRequests().subscribe(
        (users: any[]) => {
          console.log(users);
          for(var i in users){
            this.users[i] = users[i];
          }
        }
      );

      this.notifications.getAllComments().subscribe(
        (comments: any[]) => {
          console.log(comments);
          for(var i in comments){
            this.allNotifications.push(comments[i]);
          }

          this.notifications.getAllLikes().subscribe(
            (likes: any[]) => {
              console.log(likes);
              for(var i in likes){
                this.allNotifications.push(likes[i]);
              }
              this.allNotifications.sort((a,b) => (a.id < b.id) ? 1 : ((b.id < a.id) ? -1 : 0));
            }
          )

        }
      )

  
    }
    else{//show only selected article page
      this.displaySwitch = false;
      this.notifications.getArticle(+this.articleId).subscribe(
        (article: Article)=>{
          this.article = article;
          console.log("article id: ",this.article.id);
          this.home.getLike(this.article.id).subscribe(
            (like: MyLike)=>{
              console.log("like: ",like);
              if(like!=null){
                this.article.like = like;
                if(this.article.like.status == "Like"){
                  this.article.like.status = "Dislike";
                }
              }
              else{
                this.article.like = new MyLike();
                this.article.like.status = "Like";
              }
              console.log("like: ",this.article.like);
            }
          );
          this.home.getComments(this.article.id).subscribe(
            (comments: any[])=>{
              this.article.comments = comments;
            }
          );
        }
      )
    }
  }

  onPost(articleId,commentText){//Post a Comment
    console.log("articleId: "+articleId+" text: "+commentText);
    this.home.comment(articleId,commentText).subscribe();
  }

  onLike(articleId,status){
    console.log("articleId: "+articleId+" status: "+status);
    this.home.like(articleId,status).subscribe();
    if(this.article.id == articleId){
      if(this.article.like.status === "Dislike"){
        this.article.like.status = "Like";
      }
      else{
        this.article.like.status = "Dislike";
      } 
    }
  }

  Accept(email){
    for(var i = 0; i < this.users.length; i++){
      if(this.users[i].email === email){
        this.users.splice(i,1);
      }
    }
    this.notifications.Accept(email).subscribe();
  }

  Decline(email){
    for(var i = 0; i < this.users.length; i++){
      if(this.users[i].email === email){
        this.users.splice(i,1);
      }
    }
    this.notifications.Decline(email).subscribe();
  }

}
