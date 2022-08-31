import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEquestionnaire } from '../equestionnaire.model';

@Component({
  selector: 'jhi-equestionnaire-detail',
  templateUrl: './equestionnaire-detail.component.html',
})
export class EquestionnaireDetailComponent implements OnInit {
  equestionnaire: IEquestionnaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equestionnaire }) => {
      this.equestionnaire = equestionnaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
