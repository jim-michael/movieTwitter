import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { DatePipe } from '@angular/common';

import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { AllReviewsComponent } from './all-reviews/all-reviews.component';
import { MovieSearchComponent } from './movie-search/movie-search.component';
import { ReviewComponent } from './review/review.component';
import { FeaturedMovieComponent } from './featured-movie/featured-movie.component';

@NgModule({
  declarations: [
    AppComponent,
    AllReviewsComponent,
    MovieSearchComponent,
    ReviewComponent,
    FeaturedMovieComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    // RouterModule.forRoot([
    //   {
    //     path: '',
    //     component: AllReviewsComponent,
    //   },
    // ]),
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent],
})
export class AppModule {}
