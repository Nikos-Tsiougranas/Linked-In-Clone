import { Comment } from '../entities/comment';
import { User } from '../entities/user';
import { MyLike } from '../entities/mylike';

export class Article {
  text: string;
  id: number;
  comments: Comment[];
  user: User;
  like: MyLike;
  file: string;
}
