import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ReviewService } from '../review.service';

@Component({
  selector: 'app-movie-search',
  templateUrl: './movie-search.component.html',
  styleUrls: ['./movie-search.component.css'],
})
export class MovieSearchComponent implements OnInit {
  constructor(private reviewService: ReviewService) {}

  movieSearch: string = '';

  searchResults: any = { Search: [{}] };

  selectedMovie: string = '';

  dropdownActive = false;

  displayErrorMessage = false;

  @Output() selectMovieEvent = new EventEmitter<Event>();
  @Output() filterMovieEvent = new EventEmitter<Event>();

  ngOnInit(): void {}

  // searchMovie() {
  //   this.reviewService.searchMovie(this.movieSearch).subscribe((value) => {
  //     this.searchResults = value;
  //     console.log(this.searchResults);
  //     this.dropdownActive = true;
  //   });
  // }

  searchMovie() {
    this.reviewService.searchMovie(this.movieSearch).subscribe((res) => {
      if (!res.body?.hasOwnProperty('Search')) {
        this.displayErrorMessage = true;
      } else {
        this.searchResults = res.body;
        this.displayErrorMessage = false;
      }
    });
  }

  selectMovie(event: Event) {
    this.selectMovieEvent.emit(event);
  }

  filterMovie(event: Event) {
    this.filterMovieEvent.emit(event);
  }
}
