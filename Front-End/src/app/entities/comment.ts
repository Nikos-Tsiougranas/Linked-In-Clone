import { User } from '../entities/user';
import { Article } from '../entities/article';

export class Comment {
    text: string;
    id: number;
    user: User;
    article: Article;
}