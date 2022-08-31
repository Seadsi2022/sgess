import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEunite } from '../eunite.model';

@Component({
  selector: 'jhi-eunite-detail',
  templateUrl: './eunite-detail.component.html',
})
export class EuniteDetailComponent implements OnInit {
  eunite: IEunite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eunite }) => {
      this.eunite = eunite;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
