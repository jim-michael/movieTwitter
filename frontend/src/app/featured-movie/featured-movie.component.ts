import { Component, Input, OnInit } from '@angular/core';
import { Movie } from 'Movie';
import { ReviewService } from '../review.service';

@Component({
  selector: 'app-featured-movie',
  templateUrl: './featured-movie.component.html',
  styleUrls: ['./featured-movie.component.css'],
})
export class FeaturedMovieComponent implements OnInit {
  constructor(private reviewService: ReviewService) {}

  @Input()
  event!: Event;

  movie: any = {
    Title: String,
    Actors: String,
    Director: String,
    Plot: String,
    Rated: String,
    Released: String,
    Runtime: String,
    Poster: String,
    Year: String,
  };

  ngOnInit(): void {}

  ngOnChanges() {
    if (this.event) {
      this.reviewService.getMovieById(String(this.event)).subscribe((res) => {
        this.movie = res.body;
      });
    }
  }
}
