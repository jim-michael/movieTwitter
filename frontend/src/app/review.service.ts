import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Review } from 'Review';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  constructor(private http: HttpClient) {}

  getAllReviews(): Observable<Review[]> {
    return this.http.get<Review[]>('http://localhost:8080/reviews');
  }

  getAllReviewsByMovieId(id: any): Observable<Review[]> {
    return this.http.get<Review[]>(
      `http://localhost:8080/movies/${id}/reviews`
    );
  }

  searchMovie(title: string) {
    return this.http.get(
      `http://www.omdbapi.com/?s=${title}&type="movie"&apikey=50275210`,
      {
        observe: 'response',
      }
    );
  }

  createReview(
    title: String,
    reviewText: String,
    reviewRating: String,
    reviewMovieId: String
  ) {
    return this.http.post(
      `http://localhost:8080/reviews`,
      {
        title: title,
        rating: reviewRating,
        reviewText: reviewText,
        apiId: reviewMovieId,
      },
      {
        observe: 'response',
        withCredentials: true,
      }
    );
  }

  getMovieById(id: string) {
    return this.http.get(`http://www.omdbapi.com/?i=${id}&apikey=50275210`, {
      observe: 'response',
    });
  }

  // searchMovie(title: String): Observable<
  //   {
  //     Search: {
  //       Title: string;
  //       Year: string;
  //       imdbID: string;
  //       Type: string;
  //       Poster: string;
  //     };
  //   }[]
  // > {
  //   return this.http.get<
  //     {
  //       Search: {
  //         Title: string;
  //         Year: string;
  //         imdbID: string;
  //         Type: string;
  //         Poster: string;
  //       };
  //     }[]
  //   >(`http://www.omdbapi.com/?s=${title}&type="movie"&apikey=50275210`);
  // }
}
