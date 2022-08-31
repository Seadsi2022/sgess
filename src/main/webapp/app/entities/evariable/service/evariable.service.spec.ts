import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEvariable } from '../evariable.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../evariable.test-samples';

import { EvariableService } from './evariable.service';

const requireRestSample: IEvariable = {
  ...sampleWithRequiredData,
};

describe('Evariable Service', () => {
  let service: EvariableService;
  let httpMock: HttpTestingController;
  let expectedResult: IEvariable | IEvariable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EvariableService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Evariable', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const evariable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(evariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Evariable', () => {
      const evariable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(evariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Evariable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Evariable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Evariable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEvariableToCollectionIfMissing', () => {
      it('should add a Evariable to an empty array', () => {
        const evariable: IEvariable = sampleWithRequiredData;
        expectedResult = service.addEvariableToCollectionIfMissing([], evariable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evariable);
      });

      it('should not add a Evariable to an array that contains it', () => {
        const evariable: IEvariable = sampleWithRequiredData;
        const evariableCollection: IEvariable[] = [
          {
            ...evariable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEvariableToCollectionIfMissing(evariableCollection, evariable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Evariable to an array that doesn't contain it", () => {
        const evariable: IEvariable = sampleWithRequiredData;
        const evariableCollection: IEvariable[] = [sampleWithPartialData];
        expectedResult = service.addEvariableToCollectionIfMissing(evariableCollection, evariable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evariable);
      });

      it('should add only unique Evariable to an array', () => {
        const evariableArray: IEvariable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const evariableCollection: IEvariable[] = [sampleWithRequiredData];
        expectedResult = service.addEvariableToCollectionIfMissing(evariableCollection, ...evariableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evariable: IEvariable = sampleWithRequiredData;
        const evariable2: IEvariable = sampleWithPartialData;
        expectedResult = service.addEvariableToCollectionIfMissing([], evariable, evariable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evariable);
        expect(expectedResult).toContain(evariable2);
      });

      it('should accept null and undefined values', () => {
        const evariable: IEvariable = sampleWithRequiredData;
        expectedResult = service.addEvariableToCollectionIfMissing([], null, evariable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evariable);
      });

      it('should return initial array if no Evariable is added', () => {
        const evariableCollection: IEvariable[] = [sampleWithRequiredData];
        expectedResult = service.addEvariableToCollectionIfMissing(evariableCollection, undefined, null);
        expect(expectedResult).toEqual(evariableCollection);
      });
    });

    describe('compareEvariable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEvariable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEvariable(entity1, entity2);
        const compareResult2 = service.compareEvariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEvariable(entity1, entity2);
        const compareResult2 = service.compareEvariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEvariable(entity1, entity2);
        const compareResult2 = service.compareEvariable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
