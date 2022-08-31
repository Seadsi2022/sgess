import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISstructure } from '../sstructure.model';

@Component({
  selector: 'jhi-sstructure-detail',
  templateUrl: './sstructure-detail.component.html',
})
export class SstructureDetailComponent implements OnInit {
  sstructure: ISstructure | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sstructure }) => {
      this.sstructure = sstructure;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
