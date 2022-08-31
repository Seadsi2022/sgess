import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEeventualite } from '../eeventualite.model';

@Component({
  selector: 'jhi-eeventualite-detail',
  templateUrl: './eeventualite-detail.component.html',
})
export class EeventualiteDetailComponent implements OnInit {
  eeventualite: IEeventualite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eeventualite }) => {
      this.eeventualite = eeventualite;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
