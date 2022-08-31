import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISlocalite } from '../slocalite.model';

@Component({
  selector: 'jhi-slocalite-detail',
  templateUrl: './slocalite-detail.component.html',
})
export class SlocaliteDetailComponent implements OnInit {
  slocalite: ISlocalite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ slocalite }) => {
      this.slocalite = slocalite;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
